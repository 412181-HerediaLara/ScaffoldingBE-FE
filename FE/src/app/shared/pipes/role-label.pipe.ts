import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'roleLabel', standalone: true })
export class RoleLabelPipe implements PipeTransform {
  transform(value: string | null): string {
    switch (value) {
      case 'ADMIN': return 'Administrador';
      case 'USER':  return 'Usuario';
      default:      return '';
    }
  }
}
