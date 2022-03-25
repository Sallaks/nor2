import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Developer from './developer';
import DeveloperDetail from './developer-detail';
import DeveloperUpdate from './developer-update';
import DeveloperDeleteDialog from './developer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DeveloperUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DeveloperUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DeveloperDetail} />
      <ErrorBoundaryRoute path={match.url} component={Developer} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DeveloperDeleteDialog} />
  </>
);

export default Routes;
