package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CommandeDetails.
 */
@Table("commande_details")
public class CommandeDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("prix")
    private Double prix;

    @Column("etat")
    private String etat;

    @Transient
    @JsonIgnoreProperties(value = { "commandeDetails", "livreur", "client" }, allowSetters = true)
    private Commande commande;

    @Transient
    @JsonIgnoreProperties(value = { "commandeDetails", "menu", "typePlat" }, allowSetters = true)
    private Plat plat;

    @Column("commande_id")
    private Long commandeId;

    @Column("plat_id")
    private Long platId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CommandeDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrix() {
        return this.prix;
    }

    public CommandeDetails prix(Double prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getEtat() {
        return this.etat;
    }

    public CommandeDetails etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Commande getCommande() {
        return this.commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
        this.commandeId = commande != null ? commande.getId() : null;
    }

    public CommandeDetails commande(Commande commande) {
        this.setCommande(commande);
        return this;
    }

    public Plat getPlat() {
        return this.plat;
    }

    public void setPlat(Plat plat) {
        this.plat = plat;
        this.platId = plat != null ? plat.getId() : null;
    }

    public CommandeDetails plat(Plat plat) {
        this.setPlat(plat);
        return this;
    }

    public Long getCommandeId() {
        return this.commandeId;
    }

    public void setCommandeId(Long commande) {
        this.commandeId = commande;
    }

    public Long getPlatId() {
        return this.platId;
    }

    public void setPlatId(Long plat) {
        this.platId = plat;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandeDetails)) {
            return false;
        }
        return id != null && id.equals(((CommandeDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeDetails{" +
            "id=" + getId() +
            ", prix=" + getPrix() +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
