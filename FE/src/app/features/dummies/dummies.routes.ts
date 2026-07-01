import { Routes } from '@angular/router';

export default [
  { path: '',           loadComponent: () => import('./dummy-list/dummy-list').then(m => m.DummyList) },
  { path: 'new',        loadComponent: () => import('./dummy-form/dummy-form').then(m => m.DummyForm) },
  { path: ':id',        loadComponent: () => import('./dummy-detail/dummy-detail').then(m => m.DummyDetail) },
  { path: ':id/edit',   loadComponent: () => import('./dummy-form/dummy-form').then(m => m.DummyForm) }
] as Routes;
