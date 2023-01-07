package com.leords.dscatalog.domain;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User implements Serializable {

  @Serial
  private static final long serialVersionUID = -122670874990951254L;

  private Long id;

  private String firstName;

  private String lastName;

  private String email;

  private String password;
}
