/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyright [year] [name of copyright owner]".
 *
 * Copyright 2009 Sun Microsystems, Inc.
 * Portions Copyright 2015-2016 ForgeRock AS.
 */
package org.opends.guitools.controlpanel.ui;

import static org.opends.messages.AdminToolMessages.*;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;

import org.forgerock.opendj.ldap.responses.SearchResultEntry;
import org.opends.guitools.controlpanel.datamodel.BasicMonitoringAttributes;
import org.opends.guitools.controlpanel.datamodel.ServerDescriptor;
import org.opends.guitools.controlpanel.util.Utilities;
import org.opends.server.util.CollectionUtils;

/** The panel displaying the work queue monitor panel. */
 class WorkQueueMonitoringPanel extends GeneralMonitoringPanel
{
  private static final long serialVersionUID = 9031734563700069830L;
  private static final List<BasicMonitoringAttributes> attributes = CollectionUtils.newArrayList(
      BasicMonitoringAttributes.AVERAGE_REQUEST_BACKLOG,
      BasicMonitoringAttributes.MAX_REQUEST_BACKLOG,
      BasicMonitoringAttributes.CURRENT_REQUEST_BACKLOG,
      BasicMonitoringAttributes.REQUESTS_SUBMITTED,
      BasicMonitoringAttributes.REQUESTS_REJECTED);
  private final List<JLabel> monitoringLabels = new ArrayList<>();
  {
    for (int i=0; i<attributes.size(); i++)
    {
      monitoringLabels.add(Utilities.createDefaultLabel());
    }
  }

  /** Default constructor. */
  public WorkQueueMonitoringPanel()
  {
    super();
    createLayout();
  }

  @Override
  public Component getPreferredFocusComponent()
  {
    return monitoringLabels.get(0);
  }

  /** Creates the layout of the panel (but the contents are not populated here). */
  private void createLayout()
  {
    GridBagConstraints gbc = new GridBagConstraints();
    JLabel lTitle = Utilities.createTitleLabel(
        INFO_CTRL_PANEL_WORK_QUEUE.get());
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridwidth = 2;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets.top = 5;
    gbc.insets.bottom = 7;
    add(lTitle, gbc);

    gbc.insets.bottom = 0;
    gbc.insets.top = 10;
    gbc.gridy ++;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridwidth = 1;
    for (int i=0; i<attributes.size(); i++)
    {
      JLabel l = Utilities.createPrimaryLabel(getLabel(attributes.get(i)));
      gbc.gridy ++;
      gbc.insets.left = 0;
      gbc.gridx = 0;
      gbc.weightx = 0.0;
      gbc.gridwidth = 1;
      add(l, gbc);
      gbc.insets.left = 10;
      gbc.gridx = 1;
      gbc.gridwidth = 2;
      add(monitoringLabels.get(i), gbc);
    }

    gbc.gridx = 0;
    gbc.gridy ++;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.gridwidth = 3;
    add(Box.createGlue(), gbc);

    setBorder(PANEL_BORDER);
  }

  /** Updates the contents of the panel. */
  @Override
  public void updateContents()
  {
    ServerDescriptor server = null;
    if (getInfo() != null)
    {
      server = getInfo().getServerDescriptor();
    }
    SearchResultEntry sr = null;
    if (server != null)
    {
      sr = server.getWorkQueueMonitor();
    }
    if (sr != null)
    {
      updateMonitoringInfo(attributes, monitoringLabels, sr);
    }
    else
    {
      for (JLabel l : monitoringLabels)
      {
        l.setText(NO_VALUE_SET.toString());
      }
    }
  }
}
