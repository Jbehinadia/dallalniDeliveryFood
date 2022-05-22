import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommande } from '../commande.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { CommandeService } from '../service/commande.service';
import Swals2 from 'sweetalert2';
import { LivreurService } from 'app/entities/livreur/service/livreur.service';
import { ILivreur } from 'app/entities/livreur/livreur.model';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-commande',
  templateUrl: './commande.component.html',
})
export class CommandeComponent implements OnInit {
  commandes?: ICommande[];
  otherCommandes?: ICommande[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  livreur: ILivreur = {};

  constructor(
    protected clientService: ClientService,
    protected commandeService: CommandeService,
    protected livreurService: LivreurService,
    protected activatedRoute: ActivatedRoute,
    private accountService: AccountService,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      this.getLivreur(account!);
    });
  }

  getLivreur(account: Account): void {
    this.livreurService.find(account.livreur!).subscribe((resLivreur: HttpResponse<ILivreur>) => {
      this.livreur = resLivreur.body!;
      this.handleNavigation();
    });
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.commandeService
      .query({
        'livreurId.equals': this.livreur.id,
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ICommande[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  trackId(_index: number, item: ICommande): number {
    return item.id!;
  }

  associerCommande(commande: ICommande): void {
    commande.livreur = this.livreur!;
    Swals2.fire({
      title: 'associer cette commande',
      text: 'vous êtes sûr de vouloir associer cette commande?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#ff8200',
    }).then(() => this.commandeService.update(commande).subscribe());
  }

  delete(commande: ICommande): void {
    commande.livreur = {};
    Swals2.fire({
      title: 'dissocier cette commande',
      text: 'vous êtes sûr de vouloir dissocier cette commande?',
      icon: 'error',
      showCancelButton: true,
      confirmButtonColor: '#ff8200',
    }).then(() => this.commandeService.update(commande).subscribe());
  }

  editPrixLivraison(cmd: ICommande): void {
    Swals2.fire({
      title: 'Modifier le Prix Livraison',
      input: 'number',
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      showCancelButton: true,
    }).then(res => {
      if (res.value) {
        cmd.prixLivreson = Number(res.value);
        this.commandeService.update(cmd).subscribe();
      }
    });
  }

  loadOtherCommandes(): void {
    this.otherCommandes = [];
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: ICommande[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/commande'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.commandes = data ?? [];
    this.commandes.forEach(cmd => {
      this.clientService.find(cmd.client!.id!).subscribe(res => (cmd.client = res.body!));
    });
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
