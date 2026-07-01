# SDD - Frontend (Sistema de Autenticación y Gestión)

## 1. Objetivo del Frontend
SPA Angular que consume API REST backend. Maneja autenticación JWT (access + refresh token), sesión persistente, y CRUD de entidades. UX responsive con Tailwind v3. Sin testing.

---

## 2. Stack Tecnológico (Inamovible)

| Capa | Tecnología | Versión |
| :--- | :--- | :--- |
| Framework | Angular (standalone) | ^22.0.0 |
| Lenguaje | TypeScript | ~6.0.2 |
| Estilos | Tailwind CSS | v3 |
| HTTP | RxJS (Observable) | ~7.8.0 |
| Router | @angular/router (lazy loading) | ^22.0.0 |
| Forms | @angular/forms (ReactiveFormsModule) | ^22.0.0 |
| Build | @angular/build (esbuild/Vite) | ^22.0.4 |
| Node | Node.js | 18+ |

**Nota:** Angular 22 instalado (standalone, sin NgModules). Tailwind v3 requiere instalación adicional (postcss, autoprefixer, tailwindcss v3, tailwind.config.js).

---

## 3. Estructura de Directorios (Obligatoria)

```
src/
├── index.html
├── main.ts                          # bootstrapApplication(App, appConfig)
├── styles.css                       # Tailwind directives (@tailwind base/components/utilities)
│
└── app/
    ├── app.config.ts                # Router + providers config
    ├── app.routes.ts                # Rutas raíz (lazy loading a features)
    ├── app.ts / .html / .css        # Shell component (router-outlet)
    │
    ├── core/
    │   ├── guards/
    │   │   ├── auth.guard.ts        # CanActivate — redirect si no token
    │   │   └── role.guard.ts        # CanActivate — check role ADMIN
    │   ├── interceptors/
    │   │   ├── auth.interceptor.ts  # Añade Authorization: Bearer {token}
    │   │   └── error.interceptor.ts # Catch 401 → refresh o logout
    │   └── services/
    │       └── auth.service.ts      # login, register, refresh, logout, token storage
    │
    ├── shared/
    │   ├── components/              # Componentes reutilizables (botones, modales, etc.)
    │   ├── pipes/                   # Ej. capitalize, date-format
    │   └── directives/              # Ej. has-role, click-outside
    │
    ├── models/
    │   ├── auth.model.ts            # AuthResponse, LoginRequest, RegisterRequest
    │   ├── user.model.ts            # UserResponse
    │   ├── dummy.model.ts           # DummyRequest, DummyResponse
    │   └── error.model.ts           # ErrorResponse
    │
    ├── layouts/
    │   ├── auth-layout/             # Layout minimalista (login/register)
    │   │   └── auth-layout.ts / .html / .css
    │   └── main-layout/             # Layout logged in (header + sidebar + content)
    │       └── main-layout.ts / .html / .css
    │
    └── features/
        ├── auth/
        │   ├── login/
        │   │   └── login.ts / .html / .css
        │   └── register/
        │       └── register.ts / .html / .css
        ├── dashboard/
        │   └── dashboard.ts / .html / .css
        └── dummies/
            ├── dummy-list/
            │   └── dummy-list.ts / .html / .css
            ├── dummy-form/
            │   └── dummy-form.ts / .html / .css
            └── dummy-detail/
                └── dummy-detail.ts / .html / .css
```

Cada feature tiene su propio archivo de rutas (`.routes.ts`) para lazy loading.

---

## 4. Routing (app.routes.ts)

```typescript
export const routes: Routes = [
  {
    path: 'auth',
    component: AuthLayoutComponent,
    children: [
      { path: 'login',    loadComponent: () => import('./features/auth/login/login') },
      { path: 'register', loadComponent: () => import('./features/auth/register/register') },
      { path: '**',       redirectTo: 'login' }
    ]
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', loadComponent: () => import('./features/dashboard/dashboard') },
      { path: 'dummies',   loadChildren: () => import('./features/dummies/dummies.routes') },
      { path: '',          redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'auth/login' }
];
```

### 4.1. Lazy Routes — Dummies (dummies.routes.ts)

```typescript
export default [
  { path: '',           component: DummyListComponent },
  { path: 'new',        component: DummyFormComponent },
  { path: ':id',        component: DummyDetailComponent },
  { path: ':id/edit',   component: DummyFormComponent }
] satisfies Routes;
```

### 4.2. Guards

| Guard | Lógica |
| :--- | :--- |
| `authGuard` | `inject(AuthService).isAuthenticated()` ? true : `redirectTo('/auth/login')` |
| `roleGuard(role: string)` | Factory function. Check user role from token/state. Admin routes only. |

---

## 5. Flujo de Autenticación

```
[Login/Register]
      │ POST /api/auth/authenticate
      ▼
[Backend] → AuthResponse { accessToken, refreshToken, email, role }
      │
      ▼
[AuthService]
  ├─ Almacena accessToken en localStorage
  ├─ Almacena refreshToken en localStorage
  ├─ Almacena email + role en estado señal (signal)
  └─ Redirige a /dashboard

[Request protegido]
      │
      ▼
[AuthInterceptor]
  ├─ Lee accessToken de localStorage
  ├─ Añade header: Authorization: Bearer {token}
  └─ Forward request

[Response 401]
      │
      ▼
[ErrorInterceptor]
  ├─ Si hay refreshToken → POST /api/auth/refresh
  │   ├─ OK → almacena nuevos tokens → retry request original
  │   └─ FAIL → logout (limpia storage → redirect /auth/login)
  └─ Si no hay refreshToken → logout directo
```

### 5.1. AuthService (métodos clave)

```typescript
@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  // Signals reactivos
  email = signal<string | null>(null);
  role = signal<'USER' | 'ADMIN' | null>(null);

  login(req: LoginRequest): Observable<AuthResponse>
  register(req: RegisterRequest): Observable<AuthResponse>
  refresh(): Observable<AuthResponse>
  logout(): void                        // POST /api/auth/logout + limpieza local
  isAuthenticated(): boolean            // check token existence + not expired
  getToken(): string | null
  getRefreshToken(): string | null
}
```

---

## 6. Modelos TypeScript

### 6.1. auth.model.ts

```typescript
export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  name: string;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  email: string;
  role: 'USER' | 'ADMIN';
}
```

### 6.2. user.model.ts

```typescript
export interface UserResponse {
  id: number;
  email: string;
  name: string;
  role: 'USER' | 'ADMIN';
  createdAt: string;
}
```

### 6.3. dummy.model.ts

```typescript
export interface DummyRequest {
  name: string;
}

export interface DummyResponse {
  id: number;
  name: string;
  dummyEnum: string;
}
```

### 6.4. error.model.ts

```typescript
export interface ErrorResponse {
  status: number;
  error: string;
  message: string;
  path: string;
  timestamp: string;
  details?: Record<string, string[]>;
}
```

---

## 7. Servicios de Negocio

Cada entidad tiene su servicio en `core/services/` o dentro de `features/`:

```typescript
@Injectable({ providedIn: 'root' })
export class DummyService {
  private http = inject(HttpClient);
  private baseUrl = '/api/v1/dummies';

  getAll(): Observable<DummyResponse[]>;
  getById(id: number): Observable<DummyResponse>;
  create(req: DummyRequest): Observable<DummyResponse>;
  update(id: number, req: DummyRequest): Observable<DummyResponse>;
  delete(id: number): Observable<void>;
}
```

---

## 8. Componentes — Especificación

Cada componente tiene 3 archivos: `nombre.ts`, `nombre.html`, `nombre.css` (CSS solo para responsive overrides; usar Tailwind clases en HTML como fuente primaria de estilo).

### 8.1. AuthLayout

| Archivo | Propósito |
| :--- | :--- |
| `.ts` | Router outlet para páginas de auth |
| `.html` | Contenedor centrado (flex), logo/título arriba, `<router-outlet>` |
| `.css` | Responsive: padding/margins según breakpoint |

### 8.2. MainLayout

| Archivo | Propósito |
| :--- | :--- |
| `.ts` | Header con info de usuario (email, role), botón logout. Sidebar colapsable con links |
| `.html` | Header fijo top, sidebar left, `<router-outlet>` main content |
| `.css` | Responsive: sidebar oculto en mobile (<768px), toggle con menú hamburguesa |

### 8.3. Login

| Archivo | Propósito |
| :--- | :--- |
| `.ts` | ReactiveForm (email, password). Submit → `AuthService.login()`. Show error en invalid credentials |
| `.html` | Card centrada, input group, error message, link a register |
| `.css` | Responsive: full-width en mobile, max-w-md en desktop |

### 8.4. Register

| Archivo | Propósito |
| :--- | :--- |
| `.ts` | ReactiveForm (name, email, password). Validación: email format, password min 6. Submit → `AuthService.register()` |
| `.html` | Card centrada, input group, link a login |
| `.css` | Responsive: igual que login |

### 8.5. Dashboard

| Archivo | Propósito |
| :--- | :--- |
| `.ts` | Página post-login. Muestra info del usuario, stats placeholder, acceso rápido a dummies |
| `.html` | Grid de cards: bienvenida, acceso a features |
| `.css` | Responsive: grid cols 1 en mobile, 2 en tablet, 3 en desktop |

### 8.6. DummyList

| Archivo | Propósito |
| :--- | :--- |
| `.ts` | OnInit carga lista via `DummyService.getAll()`. Botón "Nuevo" → navigate `/dummies/new`. Botones editar/eliminar por fila. Confirm dialog antes de delete |
| `.html` | Tabla responsive con datos. Loading skeleton. Empty state |
| `.css` | Tabla scroll horizontal en mobile |

### 8.7. DummyForm

| Archivo | Propósito |
| :--- | :--- |
| `.ts` | ReactiveForm (name). Modo create vs edit según ruta `:id`. Submit → `create()` o `update()`. Redirect a lista tras éxito |
| `.html` | Form card. Botones guardar/cancelar |
| `.css` | Responsive: full-width form en mobile, max-w-lg en desktop |

### 8.8. DummyDetail

| Archivo | Propósito |
| :--- | :--- |
| `.ts` | Carga dummy por ID de ruta. Muestra datos en read-only. Botones editar/volver |
| `.html` | Card con detalle de campos |
| `.css` | Responsive: igual que form |

---

## 9. Pipes y Directivas (shared/)

### Pipes

| Pipe | Propósito |
| :--- | :--- |
| `capitalize` | Primera letra mayúscula |
| `truncate` | Cortar string con ellipsis |
| `roleLabel` | `USER` → "Usuario", `ADMIN` → "Administrador" |

### Directivas

| Directiva | Propósito |
| :--- | :--- |
| `hasRole` | Estructural (`*hasRole="'ADMIN'"`). Muestra/oculta elemento según rol del usuario |
| `clickOutside` | Cierra dropdowns/modales al hacer clic fuera |

---

## 10. Environment Config

### `src/environments/environment.ts`

```typescript
export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:8080',
  tokenKey: 'access_token',
  refreshTokenKey: 'refresh_token'
};
```

### `src/environments/environment.prod.ts`

```typescript
export const environment = {
  production: true,
  apiBaseUrl: '',           // Mismo origin en prod, o variable de build
  tokenKey: 'access_token',
  refreshTokenKey: 'refresh_token'
};
```

Proxy config para dev (`proxy.conf.json`):

```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false
  }
}
```

Angular CLI reference en `angular.json`: `"serve": { "options": { "proxyConfig": "proxy.conf.json" } }`.

---

## 11. Interceptores — Detalle

### 11.1. AuthInterceptor

```typescript
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private auth = inject(AuthService);

  intercept(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
    const token = this.auth.getToken();
    if (token) {
      req = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
    }
    return next(req);
  }
}
```

### 11.2. ErrorInterceptor

```typescript
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  private auth = inject(AuthService);
  private router = inject(Router);

  intercept(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
    return next(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 && !req.url.includes('/auth/')) {
          return this.auth.refresh().pipe(
            switchMap(res => {
              // retry original request con nuevo token
              const clone = req.clone({
                headers: req.headers.set('Authorization', `Bearer ${res.accessToken}`)
              });
              return next(clone);
            }),
            catchError(() => {
              this.auth.logout();
              this.router.navigate(['/auth/login']);
              return throwError(() => error);
            })
          );
        }
        return throwError(() => error);
      })
    );
  }
}
```

---

## 12. App Config (app.config.ts)

```typescript
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([authInterceptor, errorInterceptor])
    ),
    provideAnimationsAsync()   // opcional para transiciones
  ]
};
```

---

## 13. Comandos de Ejecución

```bash
# Instalar dependencias (incluir Tailwind v3)
npm install -D tailwindcss@3 postcss autoprefixer
npx tailwindcss init

# Levantar en desarrollo (Angular dev server + proxy a backend)
ng serve

# Build producción
ng build --configuration production
```

---

## 14. Restricciones Técnicas NO NEGOCIABLES

- ✅ **Sin NgModules.** Solo standalone components.
- ✅ **Sin testing.** No hay `spec.ts` funcionales (el `app.spec.ts` generado se elimina).
- ✅ **CSS solo para responsive.** Tailwind v3 clases en HTML son fuente de estilo principal.
- ✅ **3 archivos por componente:** `nombre.ts`, `nombre.html`, `nombre.css`.
- ✅ **Routing lazy loading.** No importar componentes de features directamente en el módulo raíz.
- ✅ **Interceptor funcional.** Usar `HttpInterceptorFn` (Angular 15+), no clase `HttpInterceptor` legacy.
- ✅ **Refresh token rotation.** Cada refresh invalida el refresh token anterior en backend.
- ✅ **Tokens no expuestos.** No loggear ni mostrar tokens en UI.
- ✅ **Proxy en desarrollo.** `proxy.conf.json` apunta a `localhost:8080`. En producción, backend mismo origin o CORS configurado.
- ✅ **Forms reactivos.** No usar template-driven forms.
- ✅ **Señales para estado de auth.** Signals (`email`, `role`) para reactividad, no BehaviorSubject.
