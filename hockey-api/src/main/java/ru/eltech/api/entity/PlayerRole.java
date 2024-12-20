package ru.eltech.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PlayerRole implements IEntity, Serializable {
    private Integer id;
    private String name;
}
