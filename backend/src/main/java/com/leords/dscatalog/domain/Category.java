package com.leords.dscatalog.domain;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category implements Serializable {

  @Serial
  private static final long serialVersionUID = 6304806326638055891L;

  private Long id;

  private String name;
}
