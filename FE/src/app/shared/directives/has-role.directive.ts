import { Directive, Input, TemplateRef, ViewContainerRef, inject } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';

@Directive({ selector: '[hasRole]', standalone: true })
export class HasRoleDirective {
  private auth = inject(AuthService);
  private tpl = inject(TemplateRef);
  private vcr = inject(ViewContainerRef);

  @Input() set hasRole(role: 'USER' | 'ADMIN') {
    if (this.auth.role() === role) {
      this.vcr.createEmbeddedView(this.tpl);
    } else {
      this.vcr.clear();
    }
  }
}
