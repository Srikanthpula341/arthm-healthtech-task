import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { Consultation } from '../../services/consultation';

@Component({
  selector: 'app-consultation-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './consultation-form.html',
  styleUrl: './consultation-form.css',
})
export class ConsultationForm implements OnInit {
  private fb = inject(FormBuilder);
  private consultationService = inject(Consultation);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  consultationForm = this.fb.group({
    appointmentId: ['', Validators.required],
    systolicBp: [null as number | null, [Validators.required, Validators.min(60), Validators.max(200)]],
    diastolicBp: [null as number | null, [Validators.required, Validators.min(40), Validators.max(130)]],
    temperature: [null as number | null, [Validators.required, Validators.min(35.0), Validators.max(42.0)]],
    notes: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(500)]],
    isComplete: [false, Validators.requiredTrue]
  });

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (params['appointmentId']) {
        this.consultationForm.patchValue({ appointmentId: params['appointmentId'] });
      }
    });
  }

  get notesLength() {
    return this.consultationForm.get('notes')?.value?.length || 0;
  }

  onSubmit() {
    if (this.consultationForm.valid) {
      const formValue = this.consultationForm.value;
      const dto = {
        appointmentId: formValue.appointmentId as string,
        systolicBP: Number(formValue.systolicBp),
        diastolicBP: Number(formValue.diastolicBp),
        temperature: Number(formValue.temperature),
        notes: formValue.notes!,
        isCompleted: formValue.isComplete!
      };

      this.consultationService.create(dto).subscribe({
        next: () => this.router.navigate(['/consultations']),
        error: (err) => alert('Failed to save consultation: ' + err.message)
      });
    } else {
      this.consultationForm.markAllAsTouched();
    }
  }
}
