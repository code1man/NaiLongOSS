package org.csu.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Category{
    private int id;
    private String name;
}
