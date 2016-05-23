
Open standalone.xml and follow the steps below to configure messaging services.
Add entension element
Add

1
<extension module="org.jboss.as.messaging"/>
in the <extensions> element.

Add subsystem element
Add the following <subsystem> inside <profile> element.

<subsystem xmlns="urn:jboss:domain:messaging:1.1">
            <hornetq-server>
                <persistence-enabled>true</persistence-enabled>
                <journal-file-size>102400</journal-file-size>
                <journal-min-files>2</journal-min-files>
 
                <connectors>
                    <netty-connector name="netty" socket-binding="messaging"/>
                    <netty-connector name="netty-throughput" socket-binding="messaging-throughput">
                        <param key="batch-delay" value="50"/>
                    </netty-connector>
                    <in-vm-connector name="in-vm" server-id="0"/>
                </connectors>
 
                <acceptors>
                    <netty-acceptor name="netty" socket-binding="messaging"/>
                    <netty-acceptor name="netty-throughput" socket-binding="messaging-throughput">
                        <param key="batch-delay" value="50"/>
                        <param key="direct-deliver" value="false"/>
                    </netty-acceptor>
                    <in-vm-acceptor name="in-vm" server-id="0"/>
                </acceptors>
 
                <security-settings>
                    <security-setting match="#">
                        <permission type="send" roles="guest"/>
                        <permission type="consume" roles="guest"/>
                        <permission type="createNonDurableQueue" roles="guest"/>
                        <permission type="deleteNonDurableQueue" roles="guest"/>
                    </security-setting>
                </security-settings>
 
                <address-settings>
                    <address-setting match="#">
                        <dead-letter-address>jms.queue.DLQ</dead-letter-address>
                        <expiry-address>jms.queue.ExpiryQueue</expiry-address>
                        <redelivery-delay>0</redelivery-delay>
                        <max-size-bytes>10485760</max-size-bytes>
                        <address-full-policy>BLOCK</address-full-policy>
                        <message-counter-history-day-limit>10</message-counter-history-day-limit>
                    </address-setting>
                </address-settings>
 
                <jms-connection-factories>
                    <connection-factory name="InVmConnectionFactory">
                        <connectors>
                            <connector-ref connector-name="in-vm"/>
                        </connectors>
                        <entries>
                            <entry name="java:/ConnectionFactory"/>
                        </entries>
                    </connection-factory>
                    <connection-factory name="RemoteConnectionFactory">
                        <connectors>
                            <connector-ref connector-name="netty"/>
                        </connectors>
                        <entries>
                            <entry name="RemoteConnectionFactory"/>
                            <entry name="java:jboss/exported/jms/RemoteConnectionFactory"/>
                        </entries>
                    </connection-factory>
                    <pooled-connection-factory name="hornetq-ra">
                        <transaction mode="xa"/>
                        <connectors>
                            <connector-ref connector-name="in-vm"/>
                        </connectors>
                        <entries>
                            <entry name="java:/JmsXA"/>
                        </entries>
                    </pooled-connection-factory>
                </jms-connection-factories>
 
                <jms-destinations>
                    <jms-queue name="mojQueue">
                        <entry name="queue/mojQueue"/>
                        <entry name="java:jboss/exported/jms/queue/mojQueue"/>
                    </jms-queue>
                    <jms-topic name="mojTopic">
                        <entry name="topic/mojTopic"/>
                        <entry name="java:jboss/exported/jms/topic/mojTopic"/>
                    </jms-topic>
                </jms-destinations>
            </hornetq-server>
        </subsystem>
The entry name for jms-queue should match the destination activation config property in @MessageDriven.

Add messaging port
In <socket-binding-group> add these two elements

<socket-binding name="messaging" port="5445"/>
<socket-binding name="messaging-throughput" port="5455"/>

Add MDB Resource Adapter Reference

Under <subsystem xmlns=”urn:jboss:domain:ejb3:1.2″> add this <mdb> element

<mdb>
    <resource-adapter-ref resource-adapter-name="hornetq-ra"/>
    <bean-instance-pool-ref pool-name="mdb-strict-max-pool"/>
</mdb>