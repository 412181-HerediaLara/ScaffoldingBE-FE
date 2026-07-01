import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { DummyResponse } from '../../../models/dummy.model';

@Component({
  selector: 'app-dummy-detail',
  imports: [RouterLink],
  templateUrl: './dummy-detail.html',
  styleUrl: './dummy-detail.css'
})
export class DummyDetail {
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);

  dummy = signal<DummyResponse | null>(null);
  loading = signal(true);

  constructor() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.http.get<DummyResponse>(`/api/v1/dummies/${id}`).subscribe({
        next: (data) => {
          this.dummy.set(data);
          this.loading.set(false);
        },
        error: () => this.loading.set(false)
      });
    }
  }
}
