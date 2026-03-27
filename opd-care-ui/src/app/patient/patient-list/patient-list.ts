import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Patient, PatientDto } from '../../services/patient';
import { FormsModule } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-patient-list',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './patient-list.html',
  styleUrl: './patient-list.css',
})
export class PatientList implements OnInit {
  private patientService = inject(Patient);
   private cdr = inject(ChangeDetectorRef);
  
  patients: PatientDto[] = [];
  searchQuery = '';
  
  ngOnInit() {
    this.loadPatients();
  }

  loadPatients() {
    this.patientService.getAll().subscribe({
      next: (data) => {this.patients = data || []; console.log(data); this.cdr.detectChanges(); },
      error: (err) => console.error(err)
    });
  }

  onSearch() {
    if (this.searchQuery.trim()) {
      this.patientService.search(this.searchQuery).subscribe({
        next: (data) => this.patients = data || [],
        error: (err) => console.error(err)
      });
    } else {
      this.loadPatients();
    }
  }
}
