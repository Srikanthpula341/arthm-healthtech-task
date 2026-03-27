import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { map, finalize, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { ToastService } from './toast';

@Injectable({
  providedIn: 'root',
})
export class Api {
  private http = inject(HttpClient);
  public apiUrl = 'http://localhost:8080/api/v1';
  private toastService = inject(ToastService);

  public loading = signal<boolean>(false);
  private requestCount = 0;

  private setLoading(isLoading: boolean) {
    if (isLoading) {
      this.requestCount++;
      this.loading.set(true);
    } else {
      this.requestCount--;
      if (this.requestCount <= 0) {
        this.requestCount = 0;
        this.loading.set(false);
      }
    }
  }

  private getHeaders(): HttpHeaders {
    let headers = new HttpHeaders();
    const role = localStorage.getItem('opd_role');
    const userId = localStorage.getItem('opd_user_id');
    if (role) {
      headers = headers.set('X-User-Role', role);
    }
    if (userId) {
      headers = headers.set('X-User-Id', userId);
    }
    return headers;
  }

  private handleError(err: HttpErrorResponse) {
    let msg = 'An unexpected error occurred connecting to the server.';
    if (err.error && err.error.message) {
      msg = err.error.message;
    } else if (err.message) {
      msg = err.message;
    }
    this.toastService.showError('API Request Failed', msg);
    return throwError(() => err);
  }

  get<T>(endpoint: string) {
    this.setLoading(true);
    return this.http.get<any>(`${this.apiUrl}${endpoint}`, { headers: this.getHeaders() }).pipe(
      finalize(() => this.setLoading(false)),
      map(res => { console.log('API RAW GET', res); return res?.data as T; }),
      catchError(err => this.handleError(err))
    );
  }
  post<T>(endpoint: string, body: any) {
    this.setLoading(true);
    return this.http.post<any>(`${this.apiUrl}${endpoint}`, body, { headers: this.getHeaders() }).pipe(
      finalize(() => this.setLoading(false)),
      map(res => { 
        console.log('API RAW POST', res); 
        this.toastService.showSuccess('Success', res?.message || 'Operation completed successfully!');
        return res?.data as T; 
      }),
      catchError(err => this.handleError(err))
    );
  }
  put<T>(endpoint: string, body: any) {
    this.setLoading(true);
    return this.http.put<any>(`${this.apiUrl}${endpoint}`, body, { headers: this.getHeaders() }).pipe(
      finalize(() => this.setLoading(false)),
      map(res => { 
        console.log('API RAW PUT', res); 
        this.toastService.showSuccess('Updated', res?.message || 'Updated successfully!');
        return res?.data as T; 
      }),
      catchError(err => this.handleError(err))
    );
  }
  delete<T>(endpoint: string) {
    this.setLoading(true);
    return this.http.delete<any>(`${this.apiUrl}${endpoint}`, { headers: this.getHeaders() }).pipe(
      finalize(() => this.setLoading(false)),
      map(res => { 
        console.log('API RAW DELETE', res); 
        this.toastService.showSuccess('Deleted', res?.message || 'Deleted successfully!');
        return res?.data as T; 
      }),
      catchError(err => this.handleError(err))
    );
  }
}
