import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommande } from '../../entities/commande/commande.model';

import { CommandeService } from '../../entities/commande/service/commande.service';
import { ClientService } from 'app/entities/client/service/client.service';
import { IClient } from 'app/entities/client/client.model';

@Component({
  selector: 'jhi-commande-pour-client',
  templateUrl: './commande-pour-client.component.html',
})
export class CommandePourClientComponent implements OnInit {
  commandes?: ICommande[] = [];
  client: IClient = {};
  parent!: any;
  isLoading = false;
  page?: number;

  constructor(protected clientService: ClientService, protected commandeService: CommandeService, protected modalService: NgbModal) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading = true;

    this.commandeService.query({}).subscribe({
      next: (res: HttpResponse<ICommande[]>) => {
        this.isLoading = false;
        this.commandes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }
}
