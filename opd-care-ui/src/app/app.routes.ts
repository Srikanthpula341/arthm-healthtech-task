import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Dashboard } from './dashboard/dashboard';
import { PatientList } from './patient/patient-list/patient-list';
import { PatientForm } from './patient/patient-form/patient-form';
import { AppointmentList } from './appointment/appointment-list/appointment-list';
import { AppointmentForm } from './appointment/appointment-form/appointment-form';
import { ConsultationForm } from './consultation/consultation-form/consultation-form';
import { ConsultationHistory } from './consultation/consultation-history/consultation-history';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'dashboard', component: Dashboard },
  { path: 'patients', component: PatientList },
  { path: 'patients/new', component: PatientForm },
  { path: 'appointments', component: AppointmentList },
  { path: 'appointments/new', component: AppointmentForm },
  { path: 'consultations', component: ConsultationHistory },
  { path: 'consultations/new', component: ConsultationForm },
  { path: '**', redirectTo: '/dashboard' }
];
