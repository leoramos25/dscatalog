package com.leords.dscatalog.domain;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role implements Serializable {

  @Serial
  private static final long serialVersionUID = 4754191656903635786L;

  private Long id;

  private String authority;
}
