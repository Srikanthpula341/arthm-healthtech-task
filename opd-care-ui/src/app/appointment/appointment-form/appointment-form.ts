import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { Appointment } from '../../services/appointment';
import { Patient, PatientDto } from '../../services/patient';
import { Api } from '../../services/api';
import { ToastService } from '../../services/toast';

@Component({
  selector: 'app-appointment-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './appointment-form.html',
  styleUrl: './appointment-form.css',
})
export class AppointmentForm implements OnInit {
  private fb = inject(FormBuilder);
  private appointmentService = inject(Appointment);
  private patientService = inject(Patient);
  private api = inject(Api);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  patients: PatientDto[] = [];
  doctors: any[] = [];

  appointmentForm = this.fb.group({
    patientId: ['', Validators.required],
    doctorId: ['', Validators.required],
    appointmentDate: ['', Validators.required],
    appointmentTime: ['', Validators.required]
  });

  private toastService = inject(ToastService);

  ngOnInit() {
    this.patientService.getAll().subscribe({
      next: (data) => this.patients = data || [],
      error: (err) => console.error(err)
    });
    this.api.get<any[]>('/users/role/DOCTOR').subscribe({
      next: (data) => this.doctors = data || [],
      error: (err) => console.error(err)
    });
    this.route.queryParams.subscribe(params => {
      if (params['patientId']) {
        this.appointmentForm.patchValue({ patientId: params['patientId'] });
        this.appointmentForm.get('patientId')?.disable();
      }
    });
  }

  onSubmit() {
    if (this.appointmentForm.valid) {
      const formValue = this.appointmentForm.getRawValue() as any;
      
      const selectedDate = new Date(formValue.appointmentDate!);
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      if (selectedDate < today) {
        this.toastService.showError("Validation Error", "Date must be in the future");
        return;
      }

      const time = formValue.appointmentTime!;
      const [hours, mins] = time.split(':').map(Number);
      if (hours < 9 || hours > 17 || mins !== 0) {
        this.toastService.showError("Validation Error", "Time must be between 09:00 and 17:00 in strict 1-hour intervals.");
        return;
      }

      const dto = {
        patientId: formValue.patientId as string,
        doctorId: formValue.doctorId as string,
        appointmentDate: formValue.appointmentDate!,
        appointmentTime: formValue.appointmentTime!,
        status: 'PENDING' as const
      };

      this.appointmentService.book(dto).subscribe({
        next: () => this.router.navigate(['/appointments'])
      });
    } else {
      this.appointmentForm.markAllAsTouched();
    }
  }
}
