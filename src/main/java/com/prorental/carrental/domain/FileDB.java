package com.prorental.carrental.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="files")
public class FileDB {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid2")
    private String id; //auto generated as UUID

    private String name; //Name of the file

    private String type; // mime type

    @JsonIgnore
    @Lob //datatype for storing large object data
    private byte[] data;  // array of bytes, map to a BLOB (BLOB is for storing binary data)

    public FileDB(String name, String type, byte[] data){
        this.name = name;
        this.type = type;
        this.data = data;
    }


}
