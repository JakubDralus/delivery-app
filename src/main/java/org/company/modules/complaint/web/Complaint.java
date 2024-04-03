package org.company.modules.complaint.web;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.modules.user.domain.User;

import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "p_complaint")
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String description;
    private String title;
    
    @Enumerated(EnumType.STRING)
    private ContactMethod methodOfContact;
    
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
