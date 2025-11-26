import { TestBed } from '@angular/core/testing';

import { AdminContenidoService } from './admin-contenido.service';

describe('AdminContenidoService', () => {
  let service: AdminContenidoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminContenidoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
