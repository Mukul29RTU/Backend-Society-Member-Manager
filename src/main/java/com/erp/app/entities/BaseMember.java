package com.erp.app.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseMember {

    @Id
    @Column(name = "सदस्य_नंबर")
    private Integer सदस्य_नंबर;

    @Column(name = "पूर्ण_जानकारी", columnDefinition = "TEXT")
    private String पूर्ण_जानकारी;

    @Column(name = "क्रमांक", length = 10)
    private String क्रमांक;

    @Column(name = "क्रमांक_संख्या", length = 20)
    private String क्रमांक_संख्या;

    @Column(name = "नाम", length = 255)
    private String नाम;

    @Column(name = "पहचान", length = 255)
    private String पहचान;

    @Column(name = "पता", columnDefinition = "TEXT")
    private String पता;

    @Column(name = "संपर्क")
    private Long संपर्क;

    @Column(name = "वार्ड_संख्या", length = 20)
    private String वार्ड_संख्या;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "email")
    private String email;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }
}
