import { Injectable, inject } from '@angular/core';
import { Api } from './api';
import { PatientDto } from './patient';

export interface AppointmentDto {
  id?: number;
  appointmentId?: string;
  patientId: string;
  doctorName: string;
  appointmentDate: string;
  appointmentTime: string;
  status: 'PENDING' | 'COMPLETED' | 'CANCELLED';
  patient?: PatientDto;
}

@Injectable({
  providedIn: 'root',
})
export class Appointment {
  private api = inject(Api);

  book(appointment: Partial<AppointmentDto>) {
    return this.api.post<AppointmentDto>('/appointments', appointment);
  }

  getAll() {
    return this.api.get<AppointmentDto[]>('/appointments');
  }

  getToday() {
    return this.api.get<AppointmentDto[]>('/appointments/today');
  }
}
