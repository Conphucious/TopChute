package io.github.conphucious.topchute.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tc_user")
public class UserEntity {

    @Id
    private String emailAddress;

    @NotEmpty
    private String name;

    @CreationTimestamp
    private Instant createdAt;

    public UserEntity(String emailAddress, String name) {
        this.emailAddress = emailAddress;
        this.name = name;
    }

}
