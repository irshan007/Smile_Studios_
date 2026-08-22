package com.photography.backend.entity;

public class Testimonial {
    private Long id;
    private String names;
    private String quote;
    private Integer displayOrder;

    public Testimonial() {
    }

    public Testimonial(Long id, String names, String quote, Integer displayOrder) {
        this.id = id;
        this.names = names;
        this.quote = quote;
        this.displayOrder = displayOrder;
    }

    public Testimonial(String names, String quote, Integer displayOrder) {
        this(null, names, quote, displayOrder);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
