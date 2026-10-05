import { Component, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  showBar = signal(false);
  constructor() {
    inject(Router).events.subscribe(e => {
      if (e instanceof NavigationEnd) this.showBar.set(!e.urlAfterRedirects.startsWith('/login'));
    });
  }
}
