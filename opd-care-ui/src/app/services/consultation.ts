import { Injectable, inject } from '@angular/core';
import { Api } from './api';
import { AppointmentDto } from './appointment';

export interface ConsultationDto {
  id?: number;
  consultationId?: string;
  appointmentId: string;
  systolicBP: number;
  diastolicBP: number;
  temperature: number;
  notes: string;
  isCompleted: boolean;
  appointment?: AppointmentDto;
}

@Injectable({
  providedIn: 'root',
})
export class Consultation {
  private api = inject(Api);

  create(consultation: Partial<ConsultationDto>) {
    return this.api.post<ConsultationDto>('/consultations', consultation);
  }

  getAll() {
    return this.api.get<ConsultationDto[]>('/consultations/search');
  }
}
