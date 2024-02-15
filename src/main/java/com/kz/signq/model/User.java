package com.kz.signq.model;

import com.kz.signq.model.base.BaseEntityAudit;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntityAudit {

}
