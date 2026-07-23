import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { importProvidersFrom } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { routes } from './app/app.routes';
import { authInterceptor } from './app/core/interceptors/auth.interceptor';
import { LucideAngularModule, User, Lock, LogIn } from 'lucide-angular';

const config = {
  ...appConfig,
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    importProvidersFrom(FormsModule),
    importProvidersFrom(
      LucideAngularModule.pick({ User, Lock, LogIn })
    ),

    ...(appConfig.providers ?? []),
  ],
};

bootstrapApplication(AppComponent, config).catch((err) => console.error(err));
