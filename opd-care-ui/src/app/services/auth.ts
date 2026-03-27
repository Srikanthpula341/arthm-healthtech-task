import { Injectable, signal, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Api } from './api';
import { tap } from 'rxjs/operators';

export type Role = 'ADMIN' | 'DOCTOR' | 'RECEPT' | null;

@Injectable({
  providedIn: 'root',
})
export class Auth {
  role = signal<Role>(null);
  private router = inject(Router);
  private api = inject(Api);

  constructor() {
    const savedRole = localStorage.getItem('opd_role') as Role;
    if (savedRole) {
      this.role.set(savedRole);
    }
  }

  login(credentials: {username: string, password: string}) {
    return this.api.post<any>('/auth/login', credentials).pipe(
      tap((res) => {
        if (res && res.role) {
          this.setLoggedIn(res.role);
        }
      })
    );
  }

  public setLoggedIn(role: Role) {
    this.role.set(role);
    localStorage.setItem('opd_role', role as string);
    this.router.navigate(['/dashboard']);
  }

  logout() {
    this.role.set(null);
    localStorage.removeItem('opd_role');
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.role() !== null;
  }
}
