import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRestaurant, Restaurant } from '../restaurant.model';
import { RestaurantService } from '../service/restaurant.service';

@Component({
  selector: 'jhi-restaurant-update',
  templateUrl: './restaurant-update.component.html',
})
export class RestaurantUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomRestaurant: [],
    adresseRestaurant: [],
    numRestaurant: [],
    dateOuverture: [],
    dateFermiture: [],
  });

  constructor(protected restaurantService: RestaurantService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurant }) => {
      if (restaurant.id === undefined) {
        const today = dayjs();
        restaurant.dateOuverture = today;
        restaurant.dateFermiture = today;
      }

      this.updateForm(restaurant);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurant = this.createFromForm();
    restaurant.dateOuverture = dayjs(restaurant.dateFermiture);
    restaurant.dateFermiture = dayjs(restaurant.dateFermiture);
    if (restaurant.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurantService.update(restaurant));
    } else {
      this.subscribeToSaveResponse(this.restaurantService.create(restaurant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurant>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(restaurant: IRestaurant): void {
    this.editForm.patchValue({
      id: restaurant.id,
      nomRestaurant: restaurant.nomRestaurant,
      adresseRestaurant: restaurant.adresseRestaurant,
      numRestaurant: restaurant.numRestaurant,
      dateOuverture: restaurant.dateOuverture ? restaurant.dateOuverture.format(DATE_TIME_FORMAT) : null,
      dateFermiture: restaurant.dateFermiture ? restaurant.dateFermiture.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IRestaurant {
    return {
      ...new Restaurant(),
      id: this.editForm.get(['id'])!.value,
      nomRestaurant: this.editForm.get(['nomRestaurant'])!.value,
      adresseRestaurant: this.editForm.get(['adresseRestaurant'])!.value,
      numRestaurant: this.editForm.get(['numRestaurant'])!.value,
      dateOuverture: this.editForm.get(['dateOuverture'])!.value
        ? dayjs(this.editForm.get(['dateOuverture'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dateFermiture: this.editForm.get(['dateFermiture'])!.value
        ? dayjs(this.editForm.get(['dateFermiture'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
