import { Injectable, inject } from '@angular/core';
import { Api } from './api';

export interface PatientDto {
  id?: number;
  patientId?: string;
  name: string;
  gender: string;
  age: number;
  phone: string;
}

@Injectable({
  providedIn: 'root',
})
export class Patient {
  private api = inject(Api);

  create(patient: PatientDto) {
    return this.api.post<PatientDto>('/patients', patient);
  }

  getAll() {
    return this.api.get<PatientDto[]>('/patients');
  }

  getById(id: number) {
    return this.api.get<PatientDto>(`/patients/${id}`);
  }

  search(query: string) {
    return this.api.get<PatientDto[]>(`/patients/search?query=${query}`);
  }
}
