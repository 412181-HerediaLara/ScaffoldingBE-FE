import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { DummyResponse } from '../../../models/dummy.model';

@Component({
  selector: 'app-dummy-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './dummy-form.html',
  styleUrl: './dummy-form.css'
})
export class DummyForm {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  isEdit = signal(false);
  error = signal<string | null>(null);
  loading = signal(false);

  form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(100)]]
  });

  constructor() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit.set(true);
      this.http.get<DummyResponse>(`/api/v1/dummies/${id}`).subscribe({
        next: (d) => this.form.patchValue({ name: d.name })
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading.set(true);
    this.error.set(null);

    const id = this.route.snapshot.paramMap.get('id');
    const obs = id
      ? this.http.put(`/api/v1/dummies/${id}`, this.form.getRawValue())
      : this.http.post('/api/v1/dummies', this.form.getRawValue());

    obs.subscribe({
      next: () => this.router.navigate(['/dummies']),
      error: (err) => {
        this.error.set(err.error?.message || 'Error al guardar');
        this.loading.set(false);
      }
    });
  }
}
