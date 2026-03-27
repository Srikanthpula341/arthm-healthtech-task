import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class Api {
  private http = inject(HttpClient);
  public apiUrl = 'http://localhost:8080/api/v1';

  private getHeaders(): HttpHeaders {
    let headers = new HttpHeaders();
    const role = localStorage.getItem('opd_role');
    if (role) {
      headers = headers.set('X-User-Role', role);
    }
    return headers;
  }

  get<T>(endpoint: string) {
    return this.http.get<any>(`${this.apiUrl}${endpoint}`, { headers: this.getHeaders() }).pipe(map(res => { console.log('API RAW GET', res); return res?.data as T; }));
  }
  post<T>(endpoint: string, body: any) {
    return this.http.post<any>(`${this.apiUrl}${endpoint}`, body, { headers: this.getHeaders() }).pipe(map(res => { console.log('API RAW POST', res); return res?.data as T; }));
  }
  put<T>(endpoint: string, body: any) {
    return this.http.put<any>(`${this.apiUrl}${endpoint}`, body, { headers: this.getHeaders() }).pipe(map(res => { console.log('API RAW PUT', res); return res?.data as T; }));
  }
  delete<T>(endpoint: string) {
    return this.http.delete<any>(`${this.apiUrl}${endpoint}`, { headers: this.getHeaders() }).pipe(map(res => { console.log('API RAW DELETE', res); return res?.data as T; }));
  }
}
