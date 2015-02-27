package io.aerodox.desktop.gui;

import io.aerodox.desktop.connection.JsonResponse;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.imitation.motiontools.MotionTools;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.ConnectionService;
import io.aerodox.desktop.service.MonitoringService;
import io.aerodox.desktop.service.PerformingService;
import io.aerodox.desktop.service.ServiceManager;
import io.aerodox.desktop.service.MonitoringService.StatusListener;
import io.aerodox.desktop.service.MonitoringService.StatusUpdateEvent;
import io.aerodox.desktop.translation.Action;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSlider;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class StatusWindow implements StatusListener {

	private JFrame frame;
	private JTextField hostText;
	private JLabel ipValLabel;
	private JLabel connectionStatusLabel;
	private JSlider sensitivitySlider;
	
	private Map<String , StatusUpdateHandler> handlers;
	/**
	 * Create the application.
	 */
	public StatusWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initialize();
		initUpdateHandlers();
		ServiceManager.monitoring().addStatusListener(this, getInterestedStatus());
	}
	
	
	/**
	 * Launch the application.
	 */
	public void start() {
		frame.setVisible(true);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 180);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		
		JPanel rootPanel = new JPanel();
		rootPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		rootPanel.setLayout(gridBagLayout);
		
		JLabel lblHostname = new JLabel("Hostname");
		lblHostname.setFont(new Font("Dialog", Font.BOLD, 12));
		lblHostname.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblHostname = new GridBagConstraints();
		gbc_lblHostname.anchor = GridBagConstraints.WEST;
		gbc_lblHostname.insets = new Insets(0, 0, 5, 5);
		gbc_lblHostname.gridx = 0;
		gbc_lblHostname.gridy = 0;
		rootPanel.add(lblHostname, gbc_lblHostname);
		
		hostText = new JTextField(ServiceManager.performing().getConfigGetter().getHostname());
		hostText.addKeyListener(new HostChangeListener());
		GridBagConstraints gbc_txtAerodoxHost = new GridBagConstraints();
		gbc_txtAerodoxHost.insets = new Insets(0, 0, 5, 0);
		gbc_txtAerodoxHost.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAerodoxHost.gridx = 1;
		gbc_txtAerodoxHost.gridy = 0;
		rootPanel.add(hostText, gbc_txtAerodoxHost);
		hostText.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP address");
		lblIpAddress.setFont(new Font("Dialog", Font.BOLD, 12));
		lblIpAddress.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblIpAddress = new GridBagConstraints();
		gbc_lblIpAddress.anchor = GridBagConstraints.WEST;
		gbc_lblIpAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblIpAddress.gridx = 0;
		gbc_lblIpAddress.gridy = 1;
		rootPanel.add(lblIpAddress, gbc_lblIpAddress);
		
		ipValLabel = new JLabel(ServiceManager.connection().getIP());
		GridBagConstraints gbc_lblIpvalue = new GridBagConstraints();
		gbc_lblIpvalue.anchor = GridBagConstraints.WEST;
		gbc_lblIpvalue.insets = new Insets(0, 0, 5, 0);
		gbc_lblIpvalue.gridx = 1;
		gbc_lblIpvalue.gridy = 1;
		rootPanel.add(ipValLabel, gbc_lblIpvalue);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setFont(new Font("Dialog", Font.BOLD, 12));
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.anchor = GridBagConstraints.WEST;
		gbc_lblStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 2;
		rootPanel.add(lblStatus, gbc_lblStatus);
		
		connectionStatusLabel = new JLabel("Disconnected");
		GridBagConstraints gbc_lblNoConnection = new GridBagConstraints();
		gbc_lblNoConnection.anchor = GridBagConstraints.WEST;
		gbc_lblNoConnection.insets = new Insets(0, 0, 5, 0);
		gbc_lblNoConnection.gridx = 1;
		gbc_lblNoConnection.gridy = 2;
		rootPanel.add(connectionStatusLabel, gbc_lblNoConnection);
		
		JLabel lblSensitivity = new JLabel("Sensitivity");
		lblSensitivity.setFont(new Font("Dialog", Font.BOLD, 12));
		lblSensitivity.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblSensitivity = new GridBagConstraints();
		gbc_lblSensitivity.anchor = GridBagConstraints.WEST;
		gbc_lblSensitivity.insets = new Insets(0, 0, 5, 5);
		gbc_lblSensitivity.gridx = 0;
		gbc_lblSensitivity.gridy = 3;
		rootPanel.add(lblSensitivity, gbc_lblSensitivity);
		
		sensitivitySlider = new JSlider();
		sensitivitySlider.setPaintTicks(true);
		sensitivitySlider.setMajorTickSpacing(1);
		sensitivitySlider.setSnapToTicks(true);
		sensitivitySlider.setValue(3);
		sensitivitySlider.setMaximum(10);
		sensitivitySlider.addChangeListener(new SliderChangeListener());
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.insets = new Insets(0, 0, 5, 0);
		gbc_slider.gridx = 1;
		gbc_slider.gridy = 3;
		rootPanel.add(sensitivitySlider, gbc_slider);
		
		frame.getContentPane().add(rootPanel);
		frame.pack();
	}
	
	private void initUpdateHandlers() {
		handlers = new HashMap<String, StatusUpdateHandler>();
		handlers.put("ip", new StatusUpdateHandler() {

			@Override
			public void handleNewValue(Object value) {
				ipValLabel.setText(value.toString());
			}
			
		});
		
		handlers.put("connection", new StatusUpdateHandler() {

			@Override
			public void handleNewValue(Object value) {
				connectionStatusLabel.setText(value.toString());
			}
			
		});
		
		handlers.put("sensitivity", new StatusUpdateHandler() {

			@Override
			public void handleNewValue(Object value) {
				sensitivitySlider.setValue((Integer)value);
			}
			
		});
	}
	
	private String[] getInterestedStatus() {
		return handlers.keySet().toArray(new String[handlers.size()]);
	}

	
	@Override
	public void statusUpdate(StatusUpdateEvent event) {
		this.handlers.get(event.getStatusName()).handleNewValue(event.getNewValue());
	}
	
	private interface StatusUpdateHandler {
		public abstract void handleNewValue(Object value);
	}
	
	private static class SliderChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			
			JSlider source = (JSlider)e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	int oldValue = ServiceManager.performing().getConfigGetter().getSensitivity();
	            int newValue = source.getValue();
	            if (newValue != oldValue) {
	            	ServiceManager.performing().queueAction(new ChangeSensitivityAction(newValue),
	            												ServiceManager.connection().getActiveResponseChannel());
	            }
	        }   
		}
		
		private static class ChangeSensitivityAction extends JsonResponse implements Action {
			private final int sensitivity;
			
			ChangeSensitivityAction(int newValue) {
				super("config");
				this.sensitivity = newValue;
			}

			@Override
			public JsonResponse perform(Performer performer, MotionTools tools, Configuration config) {
				config.setSensitivity(sensitivity);
				return this;
			}
			
		}
	}
	
	private class HostChangeListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			ServiceManager.performing().queueAction(new ChangeHostAction(hostText.getText()));
		}
	}
	
	private static class ChangeHostAction implements Action {
		private final String hostname;
		ChangeHostAction(String newHostname) {
			this.hostname = newHostname;
		}
		@Override
		public JsonResponse perform(Performer performer, MotionTools tools, Configuration config) {
			config.setHostname(this.hostname);
			return null;
		}
		
	}
}
