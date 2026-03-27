import { Component, inject, OnInit } from '@angular/core';
import { Auth } from '../services/auth';
import { Appointment, AppointmentDto } from '../services/appointment';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  public auth = inject(Auth);
  private appointmentService = inject(Appointment);
  
  todayAppointments: AppointmentDto[] = [];
  
  ngOnInit() {
    this.appointmentService.getToday().subscribe({
      next: (data) => this.todayAppointments = data || [],
      error: (err) => console.error(err)
    });
  }
}
