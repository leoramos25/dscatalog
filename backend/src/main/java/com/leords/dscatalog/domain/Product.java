package com.leords.dscatalog.domain;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product implements Serializable {

  @Serial
  private static final long serialVersionUID = 5644086782883094165L;

  private Long id;

  private String name;

  private String description;

  private BigDecimal price;

  private String imgUrl;
}
