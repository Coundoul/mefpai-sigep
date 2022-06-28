import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICategorieMatiere, CategorieMatiere } from '../categorie-matiere.model';
import { CategorieMatiereService } from '../service/categorie-matiere.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-categorie-matiere-update',
  templateUrl: './categorie-matiere-update.component.html',
})
export class CategorieMatiereUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    categorie: [null, [Validators.required]],
  });

  constructor(
    protected categorieMatiereService: CategorieMatiereService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    public activeModal: NgbActiveModal,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categorieMatiere }) => {
      this.updateForm(categorieMatiere);
    });
  }

  previousState(): void {
    window.history.back();
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  save(): void {
    this.isSaving = true;
    const categorieMatiere = this.createFromForm();
    if (categorieMatiere.id !== undefined) {
      this.subscribeToSaveResponse(this.categorieMatiereService.update(categorieMatiere));
    } else {
      this.subscribeToSaveResponse(this.categorieMatiereService.create(categorieMatiere));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategorieMatiere>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.router.navigate(['/equipement/new']);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(categorieMatiere: ICategorieMatiere): void {
    this.editForm.patchValue({
      id: categorieMatiere.id,
      categorie: categorieMatiere.categorie,
    });
  }

  protected createFromForm(): ICategorieMatiere {
    return {
      ...new CategorieMatiere(),
      categorie: this.editForm.get(['categorie'])!.value,
    };
  }
}
