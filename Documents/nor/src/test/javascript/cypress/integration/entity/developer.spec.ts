import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Developer e2e test', () => {
  const developerPageUrl = '/developer';
  const developerPageUrlPattern = new RegExp('/developer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const developerSample = {};

  let developer: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/developers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/developers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/developers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (developer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/developers/${developer.id}`,
      }).then(() => {
        developer = undefined;
      });
    }
  });

  it('Developers menu should load Developers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('developer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Developer').should('exist');
    cy.url().should('match', developerPageUrlPattern);
  });

  describe('Developer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(developerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Developer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/developer/new$'));
        cy.getEntityCreateUpdateHeading('Developer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', developerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/developers',
          body: developerSample,
        }).then(({ body }) => {
          developer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/developers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [developer],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(developerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Developer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('developer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', developerPageUrlPattern);
      });

      it('edit button click should load edit Developer page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Developer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', developerPageUrlPattern);
      });

      it('last delete button click should delete instance of Developer', () => {
        cy.intercept('GET', '/api/developers/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('developer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', developerPageUrlPattern);

        developer = undefined;
      });
    });
  });

  describe('new Developer page', () => {
    beforeEach(() => {
      cy.visit(`${developerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Developer');
    });

    it('should create an instance of Developer', () => {
      cy.get(`[data-cy="name"]`).type('sticky SAS Loan').should('have.value', 'sticky SAS Loan');

      cy.get(`[data-cy="age"]`).type('58655').should('have.value', '58655');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        developer = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', developerPageUrlPattern);
    });
  });
});
