import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Navbar } from './layout/navbar/navbar';
import { Auth } from './services/auth';
import { Api } from './services/api';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, Navbar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  public auth = inject(Auth);
  public api = inject(Api);
}
