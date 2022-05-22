import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ITypePlat } from 'app/entities/type-plat/type-plat.model';
import { HttpResponse } from '@angular/common/http';
import { TypePlatService } from 'app/entities/type-plat/service/type-plat.service';
import { IPlat, Plat } from 'app/entities/plat/plat.model';
import { PlatService } from 'app/entities/plat/service/plat.service';
import { MenuService } from 'app/entities/menu/service/menu.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { RestaurantService } from 'app/entities/restaurant/service/restaurant.service';
import { Restaurant } from 'app/entities/restaurant/restaurant.model';
import { ICommande } from 'app/entities/commande/commande.model';
import { ICommandeDetails } from 'app/entities/commande-details/commande-details.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  client: IClient | null = null;
  totalCommande = 0;
  typePlats?: ITypePlat[] = [];
  originPlats?: IPlat[] = [];
  Plats?: IPlat[] = [];
  commande?: ICommande = {};
  linesCmd?: ICommandeDetails[] = [];
  nbrCommandes = 0;

  private readonly destroy$ = new Subject<void>();

  constructor(
    protected commandeService: CommandeService,
    protected clientService: ClientService,
    protected restaurantService: RestaurantService,
    protected typePlatService: TypePlatService,
    protected platService: PlatService,
    protected menuService: MenuService,
    private accountService: AccountService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.totalCommande = 0;
    this.getTypePlats();
    this.getAllPlats();
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.getClientAndCommandes(account!);
      });
  }

  getClientAndCommandes(account: Account): void {
    this.clientService.find(account.client!).subscribe((resClient: HttpResponse<IClient>) => {
      this.client = resClient.body!;
      this.commandeService.query({}).subscribe(resCmd => (this.nbrCommandes = resCmd.body!.length));
    });
  }

  getAllPlats(): void {
    this.originPlats = [];
    this.Plats = [];
    this.platService.query().subscribe((resPlats: HttpResponse<IPlat[]>) => {
      this.originPlats = resPlats.body!;
      this.Plats = this.originPlats;
      this.Plats.forEach(plat => {
        if (plat.typePlat!.id) {
          this.menuService.find(plat.menu!.id!).subscribe((resMenu: HttpResponse<IMenu>) => {
            plat.menu!.nomMenu = resMenu.body!.nomMenu;
            if (resMenu.body!.restaurant!.id) {
              this.restaurantService.find(resMenu.body!.restaurant!.id).subscribe((resRestau: HttpResponse<Restaurant>) => {
                plat.nomRestau = resRestau.body?.nomRestaurant;
              });
            }
          });
        }
      });
    });
  }

  getTypePlats(): void {
    this.typePlats = [];
    this.typePlatService.query().subscribe((res: HttpResponse<ITypePlat[]>) => {
      this.typePlats = res.body ?? [];
    });
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  filterPlatByType(typeID: number): void {
    this.Plats = this.originPlats;
    if (typeID) {
      this.Plats = this.Plats?.filter(plat => plat.typePlat?.id === typeID);
    }
  }

  addLineCmd(plat: IPlat): void {
    this.totalCommande = 0;
    const index = this.linesCmd!.findIndex(lineC => lineC.plat!.id === plat.id);
    if (index === -1) {
      const newLine: ICommandeDetails = {};
      newLine.plat = plat;
      newLine.qte = 1;
      newLine.prix = plat.remisePerc ? plat.prix! - (plat.prix! * plat.remisePerc) / 100 : plat.prix;
      this.linesCmd!.push(newLine);
    } else {
      const line = this.linesCmd!.find(lineC => lineC.plat!.id === plat.id);
      line!.qte! += 1;
      line!.prix = line!.prix! * line!.qte!;
    }

    this.linesCmd!.forEach(lineC => {
      this.totalCommande += lineC.prix!;
    });
  }

  deleteLineCmd(line: ICommandeDetails): void {
    this.totalCommande = 0;
    const index = this.linesCmd!.findIndex(lineC => lineC.plat!.id === line.plat!.id);
    if (index !== -1) {
      this.linesCmd!.splice(index, 1);
    }

    this.linesCmd!.forEach(lineC => {
      this.totalCommande += lineC.prix!;
    });
  }

  changeQties(): void {
    this.totalCommande = 0;
    this.linesCmd!.forEach(lineC => {
      lineC.prix = lineC.plat!.remisePerc ? lineC.plat!.prix! - (lineC.plat!.prix! * lineC.plat!.remisePerc) / 100 : lineC.plat!.prix;
      lineC.prix = lineC.prix! * lineC.qte!;
      this.totalCommande += lineC.prix;
    });
  }
}
