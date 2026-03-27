import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Consultation, ConsultationDto } from '../../services/consultation';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-consultation-history',
  imports: [CommonModule],
  templateUrl: './consultation-history.html',
  styleUrl: './consultation-history.css',
})
export class ConsultationHistory implements OnInit {
  private consultationService = inject(Consultation);
  private cdr = inject(ChangeDetectorRef);
  consultations: ConsultationDto[] = [];

  ngOnInit() {
    this.consultationService.getAll().subscribe({
      next: (data) => { this.consultations = data || []; console.log(data); this.cdr.detectChanges(); },
      error: (err) => console.error(err)
    });
  }
}
