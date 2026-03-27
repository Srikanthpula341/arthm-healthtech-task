import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Patient } from '../../services/patient';

@Component({
  selector: 'app-patient-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './patient-form.html',
  styleUrl: './patient-form.css',
})
export class PatientForm {
  private fb = inject(FormBuilder);
  private patientService = inject(Patient);
  private router = inject(Router);

  patientForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50), Validators.pattern('^[a-zA-Z0-9 ]+$')]],
    gender: ['', Validators.required],
    age: [null as number | null, [Validators.required, Validators.min(1), Validators.max(120)]],
    phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]]
  });

  onSubmit() {
    if (this.patientForm.valid) {
      this.patientService.create(this.patientForm.value as any).subscribe({
        next: () => this.router.navigate(['/patients'])
      });
    } else {
      this.patientForm.markAllAsTouched();
    }
  }
}
