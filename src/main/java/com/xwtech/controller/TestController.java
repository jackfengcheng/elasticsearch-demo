package com.xwtech.controller;

import com.xwtech.base.ApiResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MultiMatchQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Map;


@RestController
public class TestController {

    @Autowired
    private TransportClient client;

    /**
     * ES查询文档
     * @param id id
     */

    @GetMapping("book/novel/getBook")
    public ApiResponse getBook(@RequestParam(name = "id" )  String id){
        GetResponse response = this.client.prepareGet("book", "novel", id).get();
        Map<String, Object> source = response.getSource();
        return new ApiResponse(200,"OK",source);
    }

    /**
     * ES删除文档
     * @param id id
     * @return Object
     */
    @DeleteMapping("book/novel/deleteBook")
    public ApiResponse deletBook(@RequestParam(name = "id") String id){
        DeleteResponse result = this.client.prepareDelete("book", "novel", id).get();

        return  new ApiResponse(200,"OK",result.getResult());

    }


    /**
     * ES插入文档
     * @param id ID
     * @param title 标题
     * @param author 作者
     * @param time 时间
     * @return OBJECT
     */
    @PostMapping("book/novel/saveBook")
    public ApiResponse saveBook(@RequestParam(name = "id") String id,
                                @RequestParam(name = "title") String title,
                                @RequestParam(name = "author") String author,
                                @RequestParam(name = "time")
                                    @DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss") String time
                                ){
        try {
            XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                    .field("id",id)
                    .field("title",title)
                    .field("author",author)
                    .field("time",time)
                    .endObject();
            IndexResponse result = this.client.prepareIndex("book", "novel", null).setSource(content).get();
            return new ApiResponse(200,"OK",result.getResult());
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiResponse(400,"Error");
        }
    }

    /**
     * ES插入文档
     * @param id ID
     * @param title 标题
     * @param author 作者
     * @param time 时间
     * @return OBJECT
     */
    @PutMapping("book/novel/updateBook")
    public ApiResponse updateBook(@RequestParam(name = "id") String id,
                                  @RequestParam(name = "title",required = false) String title,
                                  @RequestParam(name = "author",required = false) String author,
                                  @RequestParam(name = "time",required = false)
                                         @DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")String time){
        try {
            XContentBuilder content = XContentFactory.jsonBuilder().startObject();
            if(title != null){
                content.field("title",title);
            }
            if(author != null){
                content.field("author",author);
            }
            if(time != null){
                content.field("time",time);
            }
            content.endObject();
            UpdateRequest request =new UpdateRequest();
            request.index("book").type("novel").id(id).doc(content);
            UpdateResponse result = this.client.update(request).get();
            return new ApiResponse(200,"OK",result.getResult());
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(400,"Error");
        }
    }

    /**
     * mult批量查询
     * @return Object
     */
    @GetMapping("book/novel/multGet")
    public ApiResponse multGet(){
        MultiGetResponse result = this.client.prepareMultiGet().add("book", "novel", "1", "2")
                .add("lib", "user", "1", "2", "3").get();
        String sourceAsString =null;
        for (MultiGetItemResponse item:result ) {
            GetResponse response = item.getResponse();
            if (response !=null && response.isExists()){
                sourceAsString= response.getSourceAsString();
            }
        }
        return new ApiResponse(200,"Ok",sourceAsString);
    }

    /**
     * 查询所有
     * @return Object
     */
    @GetMapping("book/novel/searchALL")
    public  ApiResponse searchALL(){
        Map<String, Object> sources=null;
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();//查询所有
        SearchResponse result = this.client.prepareSearch("book").setQuery(queryBuilder)
                .setSize(10).get();
        SearchHits hits = result.getHits();
        for (SearchHit hit:hits) {
            hit.getSourceAsString();
            sources = hit.getSourceAsMap();
        }
        return new ApiResponse(200,"OK",sources);
    }

    /**
     * 按照条件查询
     * @param title 查询的条件
     * @return Object
     */
    @GetMapping("book/novel/matchSearch")
    public  ApiResponse matchSearch(@RequestParam(name = "title") String title){

        Map<String, Object> sourceAsMap =null;
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("title",title);//查询的参数

        SearchResponse result = this.client.prepareSearch("book").setQuery(queryBuilder)
                .setSize(10)
                .get();
        SearchHits hits = result.getHits();
        for (SearchHit hit:hits) {
            sourceAsMap = hit.getSourceAsMap();
        }
        return  new ApiResponse(200,"ok",sourceAsMap);
    }

    /**
     * 按照条件查询(多条件)
     * @param title 查询的条件
     * @param author 查询的条件
     * @param date 查询的条件
     * @return Object
     */
    @GetMapping("book/novel/multMatchSearch")
    public  ApiResponse multMatchSearch(@RequestParam(name = "title") String title,
                                        @RequestParam(name = "author") String author,
                                        @RequestParam(name = "date") String date){
        Map<String, Object> sourceAsMap =null;
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(title,author,date);
        SearchResponse result = this.client.prepareSearch("book").setQuery(queryBuilder)
                .setSize(10)
                .get();
        SearchHits hits = result.getHits();
        for (SearchHit hit:hits) {
            sourceAsMap = hit.getSourceAsMap();
        }
        return new ApiResponse(200,"OK",sourceAsMap);
    }

    /**
     * term查询
     * @param title 查询的内容
     * @return Object
     */
    @GetMapping("book/novel/termSearch")
    public  ApiResponse termSearch(@RequestParam(name = "title") String title){
        Map<String, Object> sourceAsMap =null;
        QueryBuilder queryBuilder = QueryBuilders.termQuery("title",title);
        SearchResponse result = this.client.prepareSearch("book").setQuery(queryBuilder)
                .setSize(10)
                .get();
        SearchHits hits = result.getHits();
        for (SearchHit hit:hits) {
            sourceAsMap = hit.getSourceAsMap();
        }
        return new ApiResponse(200,"OK",sourceAsMap);
    }


    @GetMapping("book/novel/termsSearch")
    public  ApiResponse termsSearch(@RequestParam(name = "title") String title,
                                    @RequestParam(name = "title1") String title1){
        Map<String, Object> sourceAsMap =null;
        QueryBuilder queryBuilder = QueryBuilders.termsQuery("title",title,title1);
        SearchResponse result = this.client.prepareSearch("book").setQuery(queryBuilder)
                .setSize(10)
                .get();
        SearchHits hits = result.getHits();
        for (SearchHit hit:hits) {
            sourceAsMap = hit.getSourceAsMap();
        }
        return new ApiResponse(200,"OK",sourceAsMap);
    }

    /**
     * 综合查询
     * @param title 查询条件
     * @return Object
     */
    @GetMapping("book/novel/querySearch")
    public  ApiResponse rangeSearch(@RequestParam(name = "title") String title){
        Map<String, Object> sourceAsMap =null;
        //范围查询
        //QueryBuilder queryBuilder = QueryBuilders.rangeQuery("birthday").format(startdate).to(enddate);
        //前缀查询
        //QueryBuilder queryBuilder = QueryBuilders.prefixQuery("title","title");
        //通配符查询
        //QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("title",title+"*");
        //模糊查询
        //QueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("title",title);
        //ids查询
        QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds("1","2","3");


        SearchResponse result = this.client.prepareSearch("book").setQuery(queryBuilder)
                .setSize(10)
                .get();
        SearchHits hits = result.getHits();
        for (SearchHit hit:hits) {
            sourceAsMap = hit.getSourceAsMap();
        }
        return new ApiResponse(200,"OK",sourceAsMap);
    }

    @GetMapping("book/novel/aggSearch")
    public  ApiResponse aggSearch(){
        //最大值
        //AggregationBuilder builder = AggregationBuilders.max("aggMax").field("count");

        //平均值
        //AggregationBuilder builder = AggregationBuilders.avg("aggAvg").field("count");


        //AggregationBuilder builder = AggregationBuilders.sum("aggSum").field("count");


        //AggregationBuilder builder = AggregationBuilders.cardinality("aggCardinality").field("count");

        //最小值
        AggregationBuilder builder = AggregationBuilders.min("aggMin").field("count");
        SearchResponse result = this.client.prepareSearch("book").addAggregation(builder).get();
        Min min = result.getAggregations().get("aggMin");
        double value = min.getValue();

        //平均值
        Avg avg =result.getAggregations().get("aggAvg");
        double value4 = avg.getValue();
        //最大值
        Max max = result.getAggregations().get("aggMax");
        double value1 = max.getValue();
        //总和
        Sum sum = result.getAggregations().get("aggSum");
        double value2 = sum.getValue();
        //基数
        Cardinality cardinality =result.getAggregations().get("aggCardinality");
        long value3 = cardinality.getValue();

        return new ApiResponse(200,"OK",value);
    }

    /**
     * queryString查询
     * @return Object
     */
    @GetMapping("book/novel/queryString")
    public ApiResponse queryString(){
        Map<String, Object> sourceAsMap =null;
        //QueryBuilder queryBuilder = QueryBuilders.commonTermsQuery("name","fengcheng");
        //QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("+changge -hejiu");
        QueryBuilder queryBuilder = QueryBuilders.simpleQueryStringQuery("+changge -hejiu");
        SearchResponse result = this.client.prepareSearch("book").setQuery(queryBuilder)
                .setSize(10)
                .get();
        SearchHits hits = result.getHits();
        for (SearchHit hit:hits) {
            sourceAsMap = hit.getSourceAsMap();
        }
        return  new ApiResponse(200,"OK",sourceAsMap);
    }

    /**
     * 组合查询 boolQuery
     * @return Object
     */
    @GetMapping("book/novel/boolQuery")
    public ApiResponse boolSQuery(){
        Map<String, Object> sourceAsMap =null;
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("name","fengcheng"))
                .mustNot(QueryBuilders.matchQuery("title","车祸"))
                .should(QueryBuilders.matchQuery("author","lisi"))
                .filter(QueryBuilders.rangeQuery("birthday")
                        .from("1992-01-01").to("2018-01-01"));

        SearchResponse response = this.client.prepareSearch("lib3").setQuery(queryBuilder).setSize(20).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
             sourceAsMap = hit.getSourceAsMap();
        }
        return new ApiResponse(200,"OK",sourceAsMap);
    }

    /**
     * 组合查询，不计算相关度分数的查询
     * @return Object
     */
    @GetMapping("book/novel/conScrollSQuery")
    public ApiResponse conScrollSQuery(){
        Map<String, Object> sourceAsMap =null;
        QueryBuilder queryBuilder = QueryBuilders.constantScoreQuery(QueryBuilders.termsQuery("name","zhsangsan"));

        SearchResponse response = this.client.prepareSearch("lib3").setQuery(queryBuilder).setSize(20).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            sourceAsMap = hit.getSourceAsMap();
        }
        return new ApiResponse(200,"OK",sourceAsMap);
    }

    /**
     * 分组聚合
     * @return Onject
     */
    @GetMapping("book/novel/term")
    public  ApiResponse term(){
        TermsAggregationBuilder field = AggregationBuilders.terms("terms").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(field).execute().actionGet();
        Terms terms = response.getAggregations().get("terms");
        for (Terms.Bucket term : terms.getBuckets()) {
            return new ApiResponse(200,"OK",term);
        }
        return new ApiResponse(200,"OK");
    }

    /**
     * 过滤聚合
     */
    @GetMapping("book/novel/term")
    public  ApiResponse filter(){
        Map<String, Object> sourceAsMap =null;
        QueryBuilder queryBuilder = QueryBuilders.termsQuery("name","张三");
        AggregationBuilder agg = AggregationBuilders.filter("filter",queryBuilder);
        SearchResponse response = this.client.prepareSearch("lib3").addAggregation(agg).execute().actionGet();
        SearchHits hits = response.getHits();
        for (SearchHit hit :
                hits) {
            sourceAsMap= hit.getSourceAsMap();
        }
        return new ApiResponse(200,"OK",sourceAsMap);
    }

    /**
     * 范围聚合
     * @return Object
     */
    @GetMapping("book/novel/range")
    public  ApiResponse range(){
        Map<String, Object> sourceAsMap =null;
        AggregationBuilder agg = AggregationBuilders.range("range")
                .field("age")
                .addUnboundedFrom(20)
                .addRange(20,50)
                .addUnboundedTo(50);
        SearchResponse response = this.client.prepareSearch("lib3").addAggregation(agg).execute().actionGet();
        SearchHits hits = response.getHits();
        for (SearchHit hit :hits ) {
            sourceAsMap= hit.getSourceAsMap();
        }
        return new ApiResponse(200,"OK",sourceAsMap);
    }

    /**
     * 为空聚合
     * @return Object
     */
    @GetMapping("book/novel/missing")
    public  ApiResponse missing(){
        Map<String, Object> sourceAsMap =null;
        AggregationBuilder agg =AggregationBuilders.missing("missing").field("price");
        SearchResponse response = this.client.prepareSearch("lib3").addAggregation(agg).execute().actionGet();
        SearchHits hits = response.getHits();
        for (SearchHit hit :
                hits) {
             sourceAsMap = hit.getSourceAsMap();
        }
        return new ApiResponse(200,"OK",sourceAsMap);
    }
}
