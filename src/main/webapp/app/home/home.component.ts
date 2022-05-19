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

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  totalCommande = 0;
  typePlats?: ITypePlat[] = [];
  Plats?: IPlat[] = [];

  private readonly destroy$ = new Subject<void>();

  constructor(
    protected restaurantService: RestaurantService,
    protected typePlatService: TypePlatService,
    protected platService: PlatService,
    protected menuService: MenuService,
    private accountService: AccountService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account;
        this.getTypePlats();
        this.getAllPlats();
      });
  }

  getAllPlats(): void {
    this.platService.query().subscribe((resPlats: HttpResponse<IPlat[]>) => {
      this.Plats = resPlats.body!;
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
}
