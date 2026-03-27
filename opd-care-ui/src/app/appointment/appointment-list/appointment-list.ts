import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Appointment, AppointmentDto } from '../../services/appointment';

@Component({
  selector: 'app-appointment-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './appointment-list.html',
  styleUrl: './appointment-list.css',
})
export class AppointmentList implements OnInit {
  private appointmentService = inject(Appointment);

  appointments = signal<AppointmentDto[]>([]);
  viewMode = signal<'ALL' | 'TODAY'>('ALL');

  ngOnInit() {
    this.loadAppointments();
  }

  loadAppointments() {
    this.viewMode.set('ALL');
    this.appointmentService.getAll().subscribe({
      next: (data) => {
        this.appointments.set(data || []);
        console.log(data);
      },
      error: (err) => console.error(err)
    });
  }

  loadToday() {
    this.viewMode.set('TODAY');
    this.appointmentService.getToday().subscribe({
      next: (data) => this.appointments.set(data || []),
      error: (err) => console.error(err)
    });
  }
}
