package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Commande.
 */
@Table("commande")
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("adresse_commande")
    private String adresseCommande;

    @Column("etat")
    private String etat;

    @Column("date_commande")
    private Instant dateCommande;

    @Column("prix_total")
    private Double prixTotal;

    @Column("remise_perc")
    private Double remisePerc;

    @Column("remice_val")
    private Double remiceVal;

    @Column("prix_livreson")
    private Double prixLivreson;

    @Column("date_sortie")
    private Instant dateSortie;

    @Transient
    private Livreur livreur;

    @Transient
    private Client client;

    @Column("livreur_id")
    private Long livreurId;

    @Column("client_id")
    private Long clientId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresseCommande() {
        return this.adresseCommande;
    }

    public Commande adresseCommande(String adresseCommande) {
        this.setAdresseCommande(adresseCommande);
        return this;
    }

    public void setAdresseCommande(String adresseCommande) {
        this.adresseCommande = adresseCommande;
    }

    public String getEtat() {
        return this.etat;
    }

    public Commande etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Instant getDateCommande() {
        return this.dateCommande;
    }

    public Commande dateCommande(Instant dateCommande) {
        this.setDateCommande(dateCommande);
        return this;
    }

    public void setDateCommande(Instant dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Double getPrixTotal() {
        return this.prixTotal;
    }

    public Commande prixTotal(Double prixTotal) {
        this.setPrixTotal(prixTotal);
        return this;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Double getRemisePerc() {
        return this.remisePerc;
    }

    public Commande remisePerc(Double remisePerc) {
        this.setRemisePerc(remisePerc);
        return this;
    }

    public void setRemisePerc(Double remisePerc) {
        this.remisePerc = remisePerc;
    }

    public Double getRemiceVal() {
        return this.remiceVal;
    }

    public Commande remiceVal(Double remiceVal) {
        this.setRemiceVal(remiceVal);
        return this;
    }

    public void setRemiceVal(Double remiceVal) {
        this.remiceVal = remiceVal;
    }

    public Double getPrixLivreson() {
        return this.prixLivreson;
    }

    public Commande prixLivreson(Double prixLivreson) {
        this.setPrixLivreson(prixLivreson);
        return this;
    }

    public void setPrixLivreson(Double prixLivreson) {
        this.prixLivreson = prixLivreson;
    }

    public Instant getDateSortie() {
        return this.dateSortie;
    }

    public Commande dateSortie(Instant dateSortie) {
        this.setDateSortie(dateSortie);
        return this;
    }

    public void setDateSortie(Instant dateSortie) {
        this.dateSortie = dateSortie;
    }

    public Livreur getLivreur() {
        return this.livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
        this.livreurId = livreur != null ? livreur.getId() : null;
    }

    public Commande livreur(Livreur livreur) {
        this.setLivreur(livreur);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
        this.clientId = client != null ? client.getId() : null;
    }

    public Commande client(Client client) {
        this.setClient(client);
        return this;
    }

    public Long getLivreurId() {
        return this.livreurId;
    }

    public void setLivreurId(Long livreur) {
        this.livreurId = livreur;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long client) {
        this.clientId = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", adresseCommande='" + getAdresseCommande() + "'" +
            ", etat='" + getEtat() + "'" +
            ", dateCommande='" + getDateCommande() + "'" +
            ", prixTotal=" + getPrixTotal() +
            ", remisePerc=" + getRemisePerc() +
            ", remiceVal=" + getRemiceVal() +
            ", prixLivreson=" + getPrixLivreson() +
            ", dateSortie='" + getDateSortie() + "'" +
            "}";
    }
}
