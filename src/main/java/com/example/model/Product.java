package com.example.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;

    @ManyToOne
    @JoinColumn(name="id_client", nullable=false)
    private Client client;
    
    @JoinTable(
        name = "relation_products",
        joinColumns = @JoinColumn(name = "id_prod1", nullable = false),
        inverseJoinColumns = @JoinColumn(name="id_prod2", nullable = false)
    )
    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<Product> products;
}