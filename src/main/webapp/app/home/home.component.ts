import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ITypePlat } from 'app/entities/type-plat/type-plat.model';
import { HttpResponse } from '@angular/common/http';
import { TypePlatService } from 'app/entities/type-plat/service/type-plat.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  totalCommande = 0;
  typePlats?: ITypePlat[] = [];

  private readonly destroy$ = new Subject<void>();

  constructor(protected typePlatService: TypePlatService, private accountService: AccountService, private router: Router) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account;
        this.getTypePlats();
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
