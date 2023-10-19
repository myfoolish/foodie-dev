package com.xw.es.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/10/28
 */
@Document(indexName = "les", type = "_doc", shards = 3, replicas = 0)
public class LEs {
    @Id
    private Long lId;

    @Field(store = true)
    private String lName;

    @Field(store = true)
    private Integer lAge;

    public Long getlId() {
        return lId;
    }

    public void setlId(Long lId) {
        this.lId = lId;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public Integer getlAge() {
        return lAge;
    }

    public void setlAge(Integer lAge) {
        this.lAge = lAge;
    }

    @Override
    public String toString() {
        return "LEs{" +
                "lId=" + lId +
                ", lName='" + lName + '\'' +
                ", lAge=" + lAge +
                '}';
    }
}
