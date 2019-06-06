package com.utstar.integral.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * created by UTSC1244 at 2019/5/24 0024
 */
@Data
@Entity
@Table(name = "CATEGORY", schema = "BTO_C2")
public class VueTreeEntity implements Serializable
{
    @Id
    @Column(name = "categoryid")
    private Integer id;

    @Column(name = "name")
    private String label;

    @Column(name = "PARENTID")
    private Integer parentId;

    @Transient
    private List<?> children;
}
