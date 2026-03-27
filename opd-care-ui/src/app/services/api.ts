import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, finalize } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class Api {
  private http = inject(HttpClient);
  public apiUrl = 'http://localhost:8080/api/v1';

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

  get<T>(endpoint: string) {
    this.setLoading(true);
    return this.http.get<any>(`${this.apiUrl}${endpoint}`, { headers: this.getHeaders() }).pipe(
      finalize(() => this.setLoading(false)),
      map(res => { console.log('API RAW GET', res); return res?.data as T; })
    );
  }
  post<T>(endpoint: string, body: any) {
    this.setLoading(true);
    return this.http.post<any>(`${this.apiUrl}${endpoint}`, body, { headers: this.getHeaders() }).pipe(
      finalize(() => this.setLoading(false)),
      map(res => { console.log('API RAW POST', res); return res?.data as T; })
    );
  }
  put<T>(endpoint: string, body: any) {
    this.setLoading(true);
    return this.http.put<any>(`${this.apiUrl}${endpoint}`, body, { headers: this.getHeaders() }).pipe(
      finalize(() => this.setLoading(false)),
      map(res => { console.log('API RAW PUT', res); return res?.data as T; })
    );
  }
  delete<T>(endpoint: string) {
    this.setLoading(true);
    return this.http.delete<any>(`${this.apiUrl}${endpoint}`, { headers: this.getHeaders() }).pipe(
      finalize(() => this.setLoading(false)),
      map(res => { console.log('API RAW DELETE', res); return res?.data as T; })
    );
  }
}
