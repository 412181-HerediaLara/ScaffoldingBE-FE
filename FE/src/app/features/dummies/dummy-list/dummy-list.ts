import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { DummyResponse } from '../../../models/dummy.model';
import { TruncatePipe } from '../../../shared/pipes/truncate.pipe';

@Component({
  selector: 'app-dummy-list',
  imports: [RouterLink, TruncatePipe],
  templateUrl: './dummy-list.html',
  styleUrl: './dummy-list.css'
})
export class DummyList {
  private http = inject(HttpClient);

  dummies = signal<DummyResponse[]>([]);
  loading = signal(true);

  constructor() {
    this.loadDummies();
  }

  loadDummies(): void {
    this.loading.set(true);
    this.http.get<DummyResponse[]>('/api/v1/dummies').subscribe({
      next: (data) => {
        this.dummies.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  delete(id: number): void {
    if (!confirm('¿Eliminar este elemento?')) return;
    this.http.delete(`/api/v1/dummies/${id}`).subscribe({
      next: () => this.loadDummies()
    });
  }
}
